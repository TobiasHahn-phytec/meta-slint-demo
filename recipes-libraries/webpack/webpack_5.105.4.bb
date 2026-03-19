SUMMARY = "Webpack is a module bundler. Its main purpose is to bundle JavaScript files for usage in a browser, yet it is also capable of transforming, bundling, or packaging just about any resource or asset."
HOMEPAGE = "https://webpack.js.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=95a881ed5cb29fc8a0fa0356525f30ac"

SRC_URI = " \
    npm://registry.npmjs.org;package=webpack;version=${PV} \
    npmsw://${THISDIR}/${BPN}/npm-shrinkwrap.json \
"
S = "${WORKDIR}/npm"

inherit npm

BBCLASSEXTEND = "native"
