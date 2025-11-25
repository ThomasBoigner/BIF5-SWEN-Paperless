import {Component} from "@angular/core";
import Keycloak, {KeycloakProfile} from "keycloak-js";
import {NGXLogger} from "ngx-logger";

@Component({
    selector: 'login-menu',
    templateUrl: './login-menu.component.html',
    styleUrls: ['./login-menu.component.css']
})
export class LoginMenuComponent {
    profile?: KeycloakProfile;

    constructor(
        private keycloak: Keycloak,
        private logger: NGXLogger
    ) {
        void this.keycloak.loadUserProfile().then(userProfile => {
            this.profile = userProfile;
        }).catch((reason: unknown) => {
            this.logger.error(reason)
        });
    }

    logout() {
        this.keycloak.logout().catch((reason: unknown) => {
            this.logger.error(reason)
        });
    }

    editProfile() {
        this.keycloak.accountManagement().catch((reason: unknown) => {
            this.logger.error(reason)
        })
    }
}