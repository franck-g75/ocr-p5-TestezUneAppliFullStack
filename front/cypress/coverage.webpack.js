
console.log('🔥 COVERAGE WEBPACK ACTIF 🔥');

module.exports = {
  module: {
    rules: [
      {
        test: /\.[jt]s$/,
        enforce: 'post',
        use: {
          loader: '@jsdevtools/coverage-istanbul-loader',
          options: {
            esModules: true
          }
        },
        include: /src/,
        exclude: [
          /\.(e2e|spec)\.ts$/,
          /node_modules/,
          /cypress/
        ],
      },
    ],
  },
};
/*
import * as path from 'path';

export default {
  module: {
    rules: [
      {
        test: /\.(js|ts)$/,
        loader: '@jsdevtools/coverage-istanbul-loader',
        options: { esModules: true },
        enforce: 'post',
        include: path.join(__dirname, '..', 'src'),
        exclude: [
          /\.(e2e|spec)\.ts$/,
          /node_modules/,
          /(ngfactory|ngstyle)\.js/,
        ],
      },
    ],
  },
};
*/