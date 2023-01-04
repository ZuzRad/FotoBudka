export const filesPayloadExists = (req, res, next) => {
	if (!req.files) return res.status(400).json({ status: '400 error', message: 'Missing files' });
	next();
};
