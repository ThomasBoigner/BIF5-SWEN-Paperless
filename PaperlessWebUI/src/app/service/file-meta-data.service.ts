import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Observable } from 'rxjs';
import { FileMetaData } from '../model/file-meta-data';
import { UploadFileCommand } from '../model/commands/upload-file-command';
import { UpdateFileCommand } from '../model/commands/update-file-command';
import {environment} from "../../environments/environment";

@Injectable({ providedIn: 'root' })
export class FileMetaDataService {
    private readonly fileMetaDataUrl: string;

    constructor(
        private http: HttpClient,
        private logger: NGXLogger,
    ) {
        this.fileMetaDataUrl = `${environment.paperlessRestUrl}/api/files`;
    }

    public getAllFileMetaData(): Observable<FileMetaData[]> {
        this.logger.debug(`Trying to get all file meta data from endpoint ${this.fileMetaDataUrl}`);
        return this.http.get<FileMetaData[]>(this.fileMetaDataUrl);
    }

    public getFileMetaData(token: string): Observable<FileMetaData> {
        this.logger.debug(
            `Trying to get file meta data with token ${token} from endpoint ${this.fileMetaDataUrl}`,
        );
        return this.http.get<FileMetaData>(`${this.fileMetaDataUrl}/${token}`);
    }

    public uploadFile(file: File, uploadFileCommand: UploadFileCommand): Observable<FileMetaData> {
        this.logger.debug(
            `Trying to upload file with command ${JSON.stringify(uploadFileCommand)} with endpoint ${this.fileMetaDataUrl}`,
        );
        const formData = new FormData();
        formData.append('file', file, file.name);
        formData.append(
            'command',
            new Blob([JSON.stringify(uploadFileCommand)], { type: 'application/json' }),
        );
        return this.http.post<FileMetaData>(this.fileMetaDataUrl, formData);
    }

    public updateFile(
        token: string,
        updateFileCommand: UpdateFileCommand,
    ): Observable<FileMetaData> {
        this.logger.debug(
            `Trying to update file with token ${token} with command ${JSON.stringify(updateFileCommand)} with endpoint ${this.fileMetaDataUrl}`,
        );
        return this.http.put<FileMetaData>(`${this.fileMetaDataUrl}/${token}`, updateFileCommand);
    }

    public deleteFile(token: string) {
        this.logger.debug(
            `Trying to delete file with token ${token} from endpoint ${this.fileMetaDataUrl}`,
        );
        return this.http.delete(`${this.fileMetaDataUrl}/${token}`);
    }
}
