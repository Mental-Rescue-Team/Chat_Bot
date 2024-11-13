const { createProxyMiddleware } = require('http-proxy-middleware');
 
module.exports = function (app) {
    app.use(
        '/',
        createProxyMiddleware({
            target: 'http://ceprj.gachon.ac.kr:60016',
            changeOrigin: true,
        })
    );
};
