export const environment = {
    paperlessRestUrl: 'http://localhost:80',
    keycloak: {
        authority: 'http://localhost:8085',
        redirectUri: 'http://localhost:4200',
        urlPattern: /^(http:\/\/localhost:80)(\/.*)?$/i,
    },
};
