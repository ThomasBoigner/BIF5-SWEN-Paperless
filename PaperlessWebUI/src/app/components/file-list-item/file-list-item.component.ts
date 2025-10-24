import { Component, input } from '@angular/core';
import { FileMetaData } from '../../model/file-meta-data';

@Component({
    selector: 'file-list-item',
    templateUrl: './file-list-item.component.html',
    styleUrls: ['./file-list-item.component.css'],
})
export class FileListItemComponent {
    fileMetaData = input.required<FileMetaData>();
}
