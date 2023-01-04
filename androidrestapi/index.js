import express from 'express';
import bodyParser from 'body-parser';
import defaultRouter from './routes/default.js';

const app = express();
const PORT = process.env.PORT || 3000;

app.use(bodyParser.json());

app.use('/api', defaultRouter);

app.listen(PORT, function () {
	console.log(`Server is running on port ${PORT}`);
});
