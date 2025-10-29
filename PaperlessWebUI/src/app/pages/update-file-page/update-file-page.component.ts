import {Component} from "@angular/core";
import {RouterLink} from "@angular/router";
import {NgOptimizedImage} from "@angular/common";
import {ErrorMessageComponent} from "../../components/error-message/error-message.component";
import {TextAreaInputComponent} from "../../components/textarea-input/textarea-input.component";
import {FileButtonComponent} from "../../components/file-button/file-button.component";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
    selector: "update-file-page",
    templateUrl: "./update-file-page.component.html",
    imports: [
        RouterLink,
        NgOptimizedImage,
        ErrorMessageComponent,
        TextAreaInputComponent,
        FileButtonComponent
    ],
    styleUrls: ["./update-file-page.component.css"]
})
export class UpdateFilePageComponent {
    errorMessage = 'Test';
    fileForm = new FormGroup({
        description: new FormControl<string | null>(null),
    })
}