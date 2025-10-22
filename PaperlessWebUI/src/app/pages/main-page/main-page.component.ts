import { Component } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FileButtonComponent } from '../../components/file-button/file-button.component';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';

@Component({
    selector: 'main-page',
    templateUrl: './main-page.component.html',
    imports: [NgOptimizedImage, RouterLink, FileButtonComponent, SearchBarComponent],
    styleUrls: ['./main-page.component.css'],
})
export class MainPageComponent {
}
