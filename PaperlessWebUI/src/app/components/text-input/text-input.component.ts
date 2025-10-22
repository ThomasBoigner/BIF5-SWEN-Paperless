import { Component, input } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ImageData } from '../image-data';

@Component({
    selector: 'text-input',
    templateUrl: './text-input.component.html',
    imports: [NgOptimizedImage, ReactiveFormsModule],
    styleUrls: ['./text-input.component.css'],
})
export class TextInputComponent {
    label = input.required<string>();
    formGroup = input.required<FormGroup>();
    controlName = input.required<string>();
    image = input<ImageData>();
}
