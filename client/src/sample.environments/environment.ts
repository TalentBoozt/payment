export const environment = {
  production: true,
  firebase: {
    apiKey: "API_KEY",
    authDomain: "AUTH_DOMAIN",
    projectId: "PROJECT_ID",
    storageBucket: "STORAGE_BUCKET",
    messagingSenderId: "MESSAGING_SENDER_ID",
    appId: "APP_ID",
    measurementId: "MESSUREMENT_ID"
  },
  apiUrl: 'https://API_DOMAIN/api/API_VERSION',
  apiUrlSimple: 'https://API_DOMAIN',
  googleAuthConfig: {
    issuer: 'https://accounts.google.com',
    redirectUri: window.location.origin +'/oauth-callback',
    clientId: 'CLIENT_ID',
    scope: 'openid profile email',
    responseType: 'id_token token',
    strictDiscoveryDocumentValidation: false,
    showDebugInformation: true,
    requireHttps: false,
    allowHttp: true,
    oidc : true
  },
  githubAuthConfig: {
    clientId: 'CLIENT_ID',
  },
  facebookAuthConfig: {
    appId: 'APP_ID',
  },
  linkedinAuthConfig: {
    redirectUri: window.location.origin + '/oauth-callback/linkedin',
    clientId: 'CLIENT_ID',
  },
  stripe_key: 'STRIPE_PUBLIC_KEY',
};
