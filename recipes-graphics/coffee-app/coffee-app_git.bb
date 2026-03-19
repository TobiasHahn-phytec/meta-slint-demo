SUMMARY = "Slint UI Coffee App Demo"
HOMEPAGE = "https://github.com/phytec/demo-slint"
LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://${S}/LICENSE;md5=3ef9d431a170ffa1941e276be63346a7 \
    file://${S}/node_modules/ws/LICENSE;md5=7a4bd929a6c0e6951846d75e53fc9f51 \
"

SRC_URI = "git://github.com/phytec/demo-slint;branch=main;protocol=https"
SRCREV = "3a3be9d088695a87accea8cbcd26fb2220037337"
PV = "0.1.0+git"
S = "${WORKDIR}/git"

SRC_URI += " \
    file://coffee-app.service \
    file://coffee-app-mock.service \
    npmsw://${THISDIR}/${BPN}/npm-shrinkwrap.json \
"

include ${BPN}-crates.inc
DEPENDS += "fontconfig openssl webpack-cli-native"

inherit npm cargo cargo-update-recipe-crates pkgconfig systemd

PACKAGES += "${PN}-mock"

SYSTEMD_SERVICE:${PN} = "coffee-app.service"
SYSTEMD_SERVICE:${PN}-mock = "coffee-app-mock.service"

# Needed for openssl to find yocto installation
export OPENSSL_DIR = "${STAGING_EXECPREFIXDIR}"
export OPENSSL_LIB_DIR = "${STAGING_LIBDIR}"
export OPENSSL_INCLUDE_DIR = "${STAGING_INCDIR}"
# Needed for skia-bindings to compile
SRC_URI += "https://github.com/rust-skia/skia-binaries/releases/download/0.78.2/skia-binaries-ec00cf219c4901d785ed-aarch64-unknown-linux-gnu-gl-textlayout.tar.gz;unpack=0;name=skia-binaries"
SRC_URI[skia-binaries.sha256sum] = "395a1eafc93943078d6c7c67ca2873e43645898b6168ed5450837ac31ced0d9c"
export SKIA_BINARIES_URL = "file://${WORKDIR}/skia-binaries-ec00cf219c4901d785ed-aarch64-unknown-linux-gnu-gl-textlayout.tar.gz"

do_compile:append() {
    export HOME=${WORKDIR}
    cd ${S}
    webpack --mode production
}

do_install:append() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${libexecdir}/${BPN}

    mv ${D}${bindir}/NextCoffee ${D}${bindir}/coffee-app
    install -m 0644 ${WORKDIR}/coffee-app.service ${D}${systemd_system_unitdir}/coffee-app.service

    install -m 0644 ${S}/dist/auto_coreservice_mock.js ${D}${libexecdir}/${BPN}
    install -m 0644 ${WORKDIR}/coffee-app-mock.service ${D}${systemd_system_unitdir}/coffee-app-mock.service
}

FILES:${PN} = " \
    ${bindir}/coffee-app \
    ${systemd_system_unitdir}/coffee-app.service \
"

FILES:${PN}-mock = " \
    ${libexecdir}/${BPN} \
    ${systemd_system_unitdir}/coffee-app-mock.service \
"
RDEPENDS:${PN} = "${PN}-mock"
RDEPENDS:${PN}-mock += "nodejs"
