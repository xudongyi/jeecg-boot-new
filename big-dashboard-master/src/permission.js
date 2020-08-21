import router from './router'



//
router.beforeEach((to, from, next) => {

        console.log(to, from, next);
        /* has token */
        if (to.path === '/') {
            next({ path: '/dashboard' })
        } else {
            next()
        }

        // if (whiteList.indexOf(to.path) !== -1) {
        //     // 在免登录白名单，直接进入
        //     next()
        // } else {
        //     next({ path: '/user/login', query: { redirect: to.fullPath } })
        //
        // }

});
