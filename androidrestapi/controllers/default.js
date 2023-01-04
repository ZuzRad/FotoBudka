import path, { dirname } from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename).replace('controllers', '');

export const uploadImages = (req, res) => {
	const files = req.files;
	const senderName = req.body.fromName;
	Object.keys(files).forEach((key) => {
		const filepath = path.join(__dirname, 'data', senderName, files[key].name);
		files[key].mv(filepath, (err) => {
			if (err) return res.status(500).json({ status: '500 error', message: err });
		});
	});

	res.json({ status: 'success', message: Object.keys(files).toString() });
};

// TEST FUNCTIONS --------------------------------

import { v4 as uuidV4 } from 'uuid';

let users = [];

export const getUsers = (req, res) => {
	res.json(users);
};

export const createUserDefaultID = (req, res) => {
	const { name, age } = req.body;
	users.push({ name, age, id: uuidV4() });
	res.json(users);
};

export const createUser = (req, res) => {
	const { id } = req.params;
	const { name, age } = req.body;
	users.push({ name, age, id });
	res.json(users);
};

export const getUser = (req, res) => {
	const { id } = req.params;
	const user = users.find((user) => user.id === id);
	res.json(user);
};

export const deleteAllUsers = (req, res) => {
	users = users.filter(() => false);
	res.json(users);
};

export const deleteUser = (req, res) => {
	const { id } = req.params;
	users = users.filter((user) => user.id !== id);
	res.json(users);
};

export const updateUser = (req, res) => {
	const { id } = req.params;
	const { name, age } = req.body;
	users = users.map((user) => {
		if (user.id !== id) return { name, age, id };
		return user;
	});
	res.json(users);
};
