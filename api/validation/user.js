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

    // Validate username: must be a non-empty string.
    if (!username || typeof username !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing username' });
    }

    // Validate email: must match a general email format.
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!email || !emailRegex.test(email)) {
        return res.status(400).json({ error: 'Invalid or missing email' });
    }

    // Validate password: must be at least 6 characters long.
    if (!password || password.length < 6) {
        return res.status(400).json({ error: 'Password must be at least 6 characters long' });
    }

    // Validate age: must be a positive number.
    if (!age || isNaN(Number(age)) || Number(age) <= 0) {
        return res.status(400).json({ error: 'Invalid or missing age' });
    }

    // Validate phone number: must be 10 to 15 numeric digits.
    const phoneNumberRegex = /^[0-9]{10,15}$/;
    if (!phoneNumber || !phoneNumberRegex.test(phoneNumber)) {
        return res.status(400).json({ error: 'Invalid or missing phone number' });
    }

    // Validate photo: must be a Buffer object, if provided.
    if (photo && !(photo instanceof Buffer)) {
        return res.status(400).json({ error: 'Invalid photo format' });
    }    

    // Validate nickname: must be a string, if provided.
    if (nickname && typeof nickname !== 'string') {
        return res.status(400).json({ error: 'Invalid nickname format' });
    }

    // Validate credit card: must be 13 to 16 numeric digits.
    const creditCardRegex = /^[0-9]{13,16}$/;
    if (!creditcard || !creditCardRegex.test(creditcard)) {
        return res.status(400).json({ error: 'Invalid or missing credit card' });
    }

    // If all validations pass, proceed to the next middleware or route handler.
    next();
};

// Export the validation function for use in routes or controllers.
module.exports = { validateUserInput };
