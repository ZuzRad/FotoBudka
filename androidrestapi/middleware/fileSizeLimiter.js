const MB = 10; // 5 MB
const FILE_SIZE_LIMIT = MB * 1024 * 1024;

export const fileSizeLimiter = (req, res, next) => {
	const files = req.files;
	const filesOverLimit = [];
	Object.keys(files).forEach((key) => {
		if (files[key].size > FILE_SIZE_LIMIT) {
			filesOverLimit.push(files[key].name);
		}
	});
	if (filesOverLimit.length) {
		return res.status(413).json({
			status: '413 error',
			message:
				`Upload failed. ${filesOverLimit.toString()} over the file size limit of ${MB} MB.`.replaceAll(
					',',
					', '
				),
		});
	}
	next();
};
