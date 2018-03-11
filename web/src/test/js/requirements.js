// Requirement for testing into NodeJS with Mocha

// document, window, and DOM
require('raf').polyfill();
require('jsdom-global/register');

// a polyfill for fetch
const isoFetch = require('isomorphic-fetch');

window.fetch = function (url, options) {
    // console.log("fetch", {url: url, options: options}, __dirname);
    if (url === 'config.json') {
        const result = require("../web/" + url);
        // console.log("get", result);
        return Promise.resolve({
            ok: true,
            text: function () {
                return Promise.resolve(JSON.stringify(result));
            },
            json: function () {
                return Promise.resolve(result);
            }
        })
    }
    return isoFetch(a, b);
};

const host = document.createElement("div");
host.classList.toggle("main-test");
document.body.append(host);

const main = document.createElement("main");
document.body.append(main);