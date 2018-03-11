const path = require("path");
const CopyWebpackPlugin = require('copy-webpack-plugin');

const resolve = jsFile => path.resolve(__dirname, `build/kotlin-js-min/main/${jsFile}.js`);

const outputPath = path.resolve(__dirname, "build/web");

const dependencies = [
    "kotlin",
    "kotlin-extensions",
    "kotlin-react",
    "kotlin-react-dom",
    "kotlinx-html-js",
];
const alias = dependencies.reduce((obj, dep) => {
    obj[dep] = resolve(dep);
    return obj;
}, {});

const copyToBuildWeb = {
    from: path.resolve(__dirname, "src/main/web/*"),
    to: outputPath,
    flatten: true
};

const mode = 'production';
const optimization = {
    minimize: true
};

const performance = {
    hints: false
};

module.exports = {
    entry: resolve("web"),
    output: {
        path: outputPath,
        filename: "bundle.js"
    },
    optimization,
    performance,
    mode,
    resolve: {alias},
    plugins: [
        new CopyWebpackPlugin([copyToBuildWeb]),
    ]
};