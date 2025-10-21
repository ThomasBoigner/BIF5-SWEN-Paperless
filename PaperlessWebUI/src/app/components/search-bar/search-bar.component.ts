import { Component, input, model } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'search-bar',
    imports: [NgOptimizedImage, FormsModule],
    templateUrl: './search-bar.component.html',
    styleUrls: ['./search-bar.component.css'],
})
export class SearchBarComponent {
    value = model('');
    placeholder = input.required<string>();
}
