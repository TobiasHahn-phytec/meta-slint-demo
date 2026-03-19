SUMMARY = "Webpack's Command Line Interface"
HOMEPAGE = "https://webpack.js.org/api/cli/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=7c6802ed94ac83214d15a26008fa22a5"

SRC_URI = " \
    npm://registry.npmjs.org;package=webpack-cli;version=${PV} \
    npmsw://${THISDIR}/${BPN}/npm-shrinkwrap.json \
"
S = "${WORKDIR}/npm"

inherit npm

BBCLASSEXTEND = "native"

RDEPENDS:${PN} = "webpack"
