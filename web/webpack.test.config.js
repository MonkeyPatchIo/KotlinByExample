const path = require("path");

const resolve = (jsFile, kind = "test") => path.resolve(__dirname, `build/kotlin-js-min/${kind}/${jsFile}.js`);

const outputPath = path.resolve(__dirname, "build/web-test");

const dependencies = [
    "kotlin",
    "kotlin-extensions",
    "kotlin-mocha",
    "kotlin-react",
    "kotlin-react-dom",
    "kotlin-test",
    "kotlinx-html-js",
];
const alias = dependencies.reduce((obj, dep) => {
    obj[dep] = resolve(dep);
    return obj;
}, {
    "web": path.resolve(__dirname, `build/classes/kotlin/main/web.js`)
});

const mode = 'development';
const optimization = {
    minimize: false
};
const performance = {
    hints: false
};

module.exports = {
    entry: resolve("web_test"),
    output: {
        path: outputPath,
        filename: "bundle-test.js"
    },
    optimization,
    performance,
    mode,
    resolve: {alias}
};