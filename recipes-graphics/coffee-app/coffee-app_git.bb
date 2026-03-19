SUMMARY = "Slint UI Coffee App Demo"
LICENSE = "MIT"
HOMEPAGE = "https://github.com/phytec/demo-slint"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=3ef9d431a170ffa1941e276be63346a7"

SRC_URI = "git://github.com/phytec/demo-slint;branch=main;protocol=https"
SRCREV = "3a3be9d088695a87accea8cbcd26fb2220037337"
PV = "0.1.0+git"
S = "${WORKDIR}/git"
CARGO_SRC_DIR = ""

SRC_URI += " \
    file://coffee-app.service \
    file://coffee-app-mock.service \
"

include ${BPN}-crates.inc
DEPENDS += "fontconfig clang-native ninja-native python3-native openssl nodejs-native"

inherit cargo cargo-update-recipe-crates pkgconfig systemd

PACKAGES += "${PN}-mock"

SYSTEMD_SERVICE:${PN} = "coffee-app.service"
SYSTEMD_SERVICE:${PN}-mock = "coffee-app-mock.service"

# Needed for openssl to find yocto installation
export OPENSSL_DIR = "${STAGING_EXECPREFIXDIR}"
export OPENSSL_LIB_DIR = "${STAGING_LIBDIR}"
export OPENSSL_INCLUDE_DIR = "${STAGING_INCDIR}"
# Needed for skia-bindings to compile
export CLANGCC = "${STAGING_BINDIR_NATIVE}/clang --target=${TARGET_SYS} --sysroot=${STAGING_DIR_TARGET}"
export CLANGCXX = "${STAGING_BINDIR_NATIVE}/clang++ --target=${TARGET_SYS} --sysroot=${STAGING_DIR_TARGET}"
export SDKTARGETSYSROOT = "${STAGING_DIR_HOST}"
export RUSTFLAGS += "-Clink-args=--sysroot=${STAGING_DIR_TARGET}"

do_compile:append() {
    export HOME=${WORKDIR}
    cd ${S}
    npm install --include dev
    npx webpack --mode production
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
RDEPENDS:${PN}-mock += "nodejs nodejs-npm"
