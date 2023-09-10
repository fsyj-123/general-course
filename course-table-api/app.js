const cheerio = require('cheerio');
const { json } = require('express/lib/response');
const fs = require('fs');

const simplifyRegex = /(\w+)\[([\u4e00-\u9fa5]+)\]\s+时\[\d+(\.\d+)?\]\s+师\[([\u4e00-\u9fa5,]+)\]/g;
const reditSplitRegex = /学分\[(\d+(\.\d+)?)\]/
const preSimplifyRegex = /\((.*?)\)\s(.*?)\[(.*?)\]$/
function generateArray(start, length, step) {
    return [...Array(length)].map((_, index) => start + index * step);
}

function buildMap(content) {
    const result = []
    // 提取课程名称和教师信息
    // 首先通过正则提取学分，然后通过split分割字符串，然后提取名字
    let reditMatch = reditSplitRegex.exec(content)
    let redit = 0
    if (reditMatch) {
        redit = reditMatch[1]
    }
    // 分割
    const splits = content.split(reditSplitRegex)
    const courseNameProperties = splits[0].trim()
    // 提取出课程简称和课程名，方便后面拼接种类
    const courseProperties = splits[3].trim()
    let matches;
    matches = preSimplifyRegex.exec(courseNameProperties)
    let preSimplifyName = ''
    let fullName = ''
    if (matches) {
        preSimplifyName = matches[1]
        fullName = matches[2]
    }
    // 对于后半部分，使用 simplifyRegex 进行提取
    const courseTypesAndTeachers = {};
    while ((matches = simplifyRegex.exec(courseProperties)) !== null) {
        const courseType = matches[1];
        const teacher = matches[4];
        result.push({
            'simplifyName': preSimplifyName + courseType,
            'course': {
                'name': fullName,
                'teacher': teacher,
                'redit': redit
            }
        })
    }
    return result
}

function parse(html) {
    const courses = []
    if (html.includes('用户登录')) {
        throw new Error("用户未登录")
    }
    const $ = cheerio.load(html)
    courseTable = $('table')
    // 构建课程简称到课程全名的映射
    const courseMap = new Map;
    const regex = /\((.*?)\)\s+(.*?)\[/;
    const teacherRegex = /师\[(.*?)\]/;
    const bTags = $(courseTable[2]).find('b')
    for (const b of bTags) {
        const bContent = $(b).parent().text().trim();

        let mapArr = buildMap(bContent)
        for (const mapItem of mapArr) {
            courseMap.set(mapItem.simplifyName, mapItem.course)
        }
    }


    const rows = $(courseTable[1]).find('tr')
    // 构建课程数组
    for (let i = 2; i < rows.length; i++) {
        const weekRow = rows[i];
        const cells = $(weekRow).find('td')
        let section = 1
        let day = 1
        for (let j = 1; j < cells.length; j++) {
            let courseInfo = $(cells[j].children[0]).text().trim();
            const classroom = $(cells[j].children[2]).text().trim();
            let mappedCourse = courseMap.get(courseInfo.trim());
            // 利用map降低重复查找次数
            if (mappedCourse) {
                const colspan = parseInt($(cells[j]).attr('colspan'));
                const course = {
                    name: mappedCourse.name,
                    position: classroom,
                    sections: generateArray(section >= 5 ? section - 1 : section, colspan, 1),
                    weeks: [i - 1],
                    'day': day,
                    teacher: mappedCourse.teacher
                };
                section += colspan;
                courses.push(course);
            } else {
                if (courseInfo !== '') {
                    section += parseInt($(cells[j]).attr('colspan'));
                } else {
                    section++;
                }
            }
            if (section >= 13) {
                day += parseInt(section / 12);
                section %= 12;
            }
        }
    }
    return courses
}

function combineCourses(courses) {
    const combinedCourses = [];

    // Create a map to group courses by their unique attributes
    const courseMap = new Map();
    for (const course of courses) {
        const { name, position, sections, day, teacher } = course;
        const key = `${name}-${position}-${sections}-${day}-${teacher}`;

        if (!courseMap.has(key)) {
            courseMap.set(key, {
                name,
                position,
                sections,
                weeks: [],
                day,
                teacher
            });
        }

        const combinedCourse = courseMap.get(key);
        combinedCourse.weeks.push(...course.weeks);
    }

    // Merge continuous weeks for each combined course
    for (const [key, combinedCourse] of courseMap) {
        combinedCourse.weeks.sort((a, b) => a - b);
        let startWeek = combinedCourse.weeks[0]
        let continuousLen = 1
        for (let i = 1; i < combinedCourse.weeks.length; i++) {
            if (combinedCourse.weeks[i] === combinedCourse.weeks[i - 1] + 1) {
                continuousLen++
            } else {
                combinedCourses.push({ ...combinedCourse, weeks: generateArray(startWeek, continuousLen, 1) });
                startWeek = combinedCourse.weeks[i];
                continuousLen = 1;
            }
        }
        combinedCourses.push({ ...combinedCourse, weeks: generateArray(startWeek, continuousLen, 1) });
    }

    return combinedCourses;
}

function exportCourse(html) {
    return new Promise((resolve, reject) => {
        try {
            let courses = parse(html);
            let courseArr = combineCourses(courses)
            resolve(JSON.stringify(courseArr));
        } catch (error) {
            reject(error);
        }
    });
}

module.exports = { exportCourse }

