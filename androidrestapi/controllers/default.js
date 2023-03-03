import path, { dirname } from 'path';
import { fileURLToPath } from 'url';
import { jsPDF } from 'jspdf';
import fs from 'fs';
import imagemin from 'imagemin';
import imageminPngquant from 'imagemin-pngquant';
import imageminOptipng from 'imagemin-optipng';
import imageminJpegtran from 'imagemin-jpegtran';
import imageminMozjpeg from 'imagemin-mozjpeg';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename).replace('controllers', '');

export const uploadImages = (req, res) => {
	const files = req.files;
	const senderName = req.body.fromName;
	const banner = req.body.banner;

	const doc = new jsPDF('p', 'mm', 'a4', true);
	let y_offset = 0;
	let x_offset = 105;
	let index = 0;

	let bannerImage = fs.readFileSync(__dirname + `banners\\build\\${banner}.jpg`);

	doc.addImage(bannerImage, 'JPEG', 90, 0, 15, 297, 'banner', 'FAST');
	doc.addImage(bannerImage, 'JPEG', 195, 0, 15, 297, 'banner', 'FAST');

	Object.keys(files).forEach((key) => {
		const filepath = path.join(__dirname, 'data', senderName, files[key].name);
		files[key].mv(filepath, (err) => {
			if (err) return res.status(500).json({ status: '500 error', message: err });
		});
	});

	(async () => {
		await imagemin([`data/${senderName}/*.{jpg,jpeg,png}`], {
			destination: `build/${senderName}`,
			plugins: [
				imageminJpegtran(),
				imageminMozjpeg({ quality: 75 }),
				imageminPngquant(),
				imageminOptipng(),
			],
		});
		Object.keys(files).forEach((key) => {
			let fileName = files[key].name;
			let file = fs.readFileSync(`${__dirname}build\\${senderName}\\${fileName}`);
			index++;
			if (index == 4) {
				x_offset += 105;
				y_offset = 0;
			}
			doc.addImage(file, 'JPEG', x_offset - 15, y_offset + 9, 99, 90, undefined, 'FAST', 90);
			y_offset += 99;
		});
		fs.rmSync(`${__dirname}build\\${senderName}`, { recursive: true, force: true });
		doc.save(`data/${senderName}/table.pdf`);
		res.json({ status: 'success', message: Object.keys(files).toString() });
	})();
};
