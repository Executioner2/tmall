// const webpack=require('webpack');
module.exports = {
  /*
  ** Headers of the page
  */
  head: {
    title: 'tmall-user',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '天猫购物' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
      { rel: 'stylesheet', href: 'https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css' },
      { rel: 'stylesheet', href: 'https://cdn.bootcss.com/twitter-bootstrap/3.3.7/css/bootstrap.min.css' }
    ],
    script:[
      {src:'https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js'},
      {src:'https://cdn.bootcss.com/twitter-bootstrap/3.3.7/js/bootstrap.min.js'},
    ]
  },
  /*
  ** Customize the progress bar color
  */
  loading: { color: '#3B8070' },
  /*
  ** Build configuration
  */
  build: {
    // plugins: [
    //   new webpack.ProvidePlugin({
    //     '$' : 'jquery'
    //   }),
    // ],
    /*
    ** Run ESLint on save
    */
    extend (config, { isDev, isClient }) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }
  },
  plugins: [
    { src: '~/plugins/element-ui.js', ssr: false }
  ]
}

