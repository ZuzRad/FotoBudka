import express from 'express';
import fileUpload from 'express-fileupload';
import {
	uploadImages,
	createUser,
	createUserDefaultID,
	getUsers,
	getUser,
	deleteUser,
	deleteAllUsers,
	updateUser,
} from '../controllers/default.js';
const router = express.Router();

import { filesPayloadExists } from '../middleware/filesPayloadExists.js';
import { fileExtLimiter } from '../middleware/fileExtLimiter.js';
import { fileSizeLimiter } from '../middleware/fileSizeLimiter.js';

router.post(
	'/upload',
	fileUpload({ createParentPath: true }),
	filesPayloadExists,
	fileExtLimiter(['.png', '.jpg', '.jpeg']),
	fileSizeLimiter,
	uploadImages
);

// TEST FUNCTIONS
router.get('/users', getUsers);
router.post('/users', createUserDefaultID);
router.delete('/users', deleteAllUsers);
router.post('/users/:id', createUser);
router.get('/users/:id', getUser);
router.delete('/users/:id', deleteUser);
router.patch('/users/:id', updateUser);

// POST = CREATE
// PATCH = UPDATE
// PUT = UPDATE OR CREATE

export default router;
