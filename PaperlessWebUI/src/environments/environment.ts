export const environment = {
    paperlessRestUrl: 'http://localhost:80',
    keycloak: {
        authority: 'http://localhost:8084',
        redirectUri: 'http://localhost:4200',
        urlPattern: /^(http:\/\/localhost:80)(\/.*)?$/i,
    },
};
