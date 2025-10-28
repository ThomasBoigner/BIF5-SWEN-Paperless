import { Component, input } from '@angular/core';

@Component({
    selector: 'small-file-button',
    templateUrl: './small-file-button.component.html',
    styleUrls: ['./small-file-button.component.css'],
})
export class SmallFileButton {
    text = input.required<string>();
}
