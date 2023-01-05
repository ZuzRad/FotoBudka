import path, { dirname } from 'path';
import { fileURLToPath } from 'url';
import { jsPDF } from 'jspdf';
import 'jspdf-autotable';
import fs from 'fs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename).replace('controllers', '');

export const uploadImages = (req, res) => {
	const files = req.files;
	const senderName = req.body.fromName;
	const banner = req.body.banner;

	const doc = new jsPDF('p', 'mm', 'a4', true);
	let y_offset = 0;
	let x_offset = 0;
	let index = 0;
	let bannerImage = fs.readFileSync(__dirname + `banners\\${banner}.jpg`);
	doc.addImage(bannerImage, 'JPEG', 90, 0, 15, 297, 'banner', 'FAST');
	doc.addImage(bannerImage, 'JPEG', 195, 0, 15, 297, 'banner', 'FAST');

	Object.keys(files).forEach((key) => {
		index++;
		if (index == 4) {
			x_offset += 105;
			y_offset = 0;
		}

		const filepath = path.join(__dirname, 'data', senderName, files[key].name);
		doc.addImage(files[key].data, 'JPEG', x_offset, y_offset, 90, 99, undefined, 'FAST');
		y_offset += 99;
		files[key].mv(filepath, (err) => {
			if (err) return res.status(500).json({ status: '500 error', message: err });
		});
	});

	doc.save(`data/${senderName}/table.pdf`);
	res.json({ status: 'success', message: Object.keys(files).toString() });
};
