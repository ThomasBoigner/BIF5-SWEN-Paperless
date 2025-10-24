import { Component, input } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ImageData } from '../image-data';

@Component({
    selector: 'textarea-input',
    templateUrl: './textarea-input.component.html',
    imports: [NgOptimizedImage, ReactiveFormsModule],
    styleUrls: ['./textarea-input.component.css'],
})
export class TextAreaInputComponent {
    label = input.required<string>();
    formGroup = input.required<FormGroup>();
    controlName = input.required<string>();
    image = input<ImageData>();
}
