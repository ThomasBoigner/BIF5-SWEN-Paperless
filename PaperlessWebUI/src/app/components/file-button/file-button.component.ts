import { Component, input } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { ImageData } from '../image-data';

@Component({
    selector: 'tour-button',
    templateUrl: './file-button.component.html',
    imports: [NgOptimizedImage],
    styleUrls: ['./file-button.component.css'],
})
export class FileButtonComponent {
    text = input.required<string>();
    color = input.required<'primary' | 'secondary' | 'error'>();
    primaryImage = input<ImageData>();
    secondaryImage = input<ImageData>();
}
