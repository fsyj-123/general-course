const express = require('express');
const {exportCourse} = require('./app.js')
const app = express();

app.use(express.json());

app.post('/export', (req, res) => {
    const html = req.body.html;
    exportCourse(html)
        .then(result => {
            res.send(result);
        })
        .catch(error => {
            console.error(error);
            res.status(500).send('Internal Server Error');
        });
});


app.listen(3000, () => {
    console.log('Server is running on port 3000');
});
