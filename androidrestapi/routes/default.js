import express from 'express';
import fileUpload from 'express-fileupload';
import { uploadImages } from '../controllers/default.js';
import { filesPayloadExists } from '../middleware/filesPayloadExists.js';
import { fileExtLimiter } from '../middleware/fileExtLimiter.js';
import { fileSizeLimiter } from '../middleware/fileSizeLimiter.js';
const router = express.Router();

router.post(
	'/upload',
	fileUpload({ createParentPath: true }),
	filesPayloadExists,
	fileExtLimiter(['.png', '.jpg', '.jpeg']),
	fileSizeLimiter,
	uploadImages
);

export default router;
