const validateUserInput = (req, res, next) => {
    const {
        username,
        email,
        password,
        age,
        contry,
        phoneNumber,
        photo,
        nickname,
        creditcard,
        watchedMovies
    } = req.body;

    if (!username || typeof username !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing username' });
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!email || !emailRegex.test(email)) {
        return res.status(400).json({ error: 'Invalid or missing email' });
    }

    if (!password || password.length < 6) {
        return res.status(400).json({ error: 'Password must be at least 6 characters long' });
    }

    if (!age || isNaN(Number(age)) || Number(age) <= 0) {
        return res.status(400).json({ error: 'Invalid or missing age' });
    }

    const phoneNumberRegex = /^[0-9]{10,15}$/;
    if (!phoneNumber || !phoneNumberRegex.test(phoneNumber)) {
        return res.status(400).json({ error: 'Invalid or missing phone number' });
    }

    if (photo && !(photo instanceof Buffer)) {
        return res.status(400).json({ error: 'Invalid photo format' });
    }    

    if (nickname && typeof nickname !== 'string') {
        return res.status(400).json({ error: 'Invalid nickname format' });
    }

    const creditCardRegex = /^[0-9]{13,16}$/;
    if (!creditcard || !creditCardRegex.test(creditcard)) {
        return res.status(400).json({ error: 'Invalid or missing credit card' });
    }

    next();
};

module.exports = { validateUserInput };
