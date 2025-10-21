import { Component, input } from '@angular/core';

@Component({
    selector: 'error-message',
    templateUrl: './error-message.component.html',
    styleUrls: ['./error-message.component.css'],
})
export class ErrorMessageComponent {
    text = input<string>();
}
