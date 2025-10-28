import { Component, input } from '@angular/core';
import { FileMetaData } from '../../model/file-meta-data';
import {RouterLink} from "@angular/router";

@Component({
    selector: 'file-list-item',
    templateUrl: './file-list-item.component.html',
    styleUrls: ['./file-list-item.component.css'],
    imports: [
        RouterLink
    ]
})
export class FileListItemComponent {
    fileMetaData = input.required<FileMetaData>();
    isSelected = input.required<boolean>();
}
