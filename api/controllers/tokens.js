const tokensService = require('../services/tokens');

const autentication = async (req, res) => {
    const userId = await tokensService.isExist(req.body.username, req.body.password);
    if (!userId) {
        return res.status(404).json({ errors: ['User not found'] });
    }
    res.status(200).json(userId);
};

module.exports = {autentication};