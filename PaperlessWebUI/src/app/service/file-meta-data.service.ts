import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {NGXLogger} from "ngx-logger";
import {Observable} from "rxjs";
import {FileMetaData} from "../model/file-meta-data";

@Injectable({providedIn: 'root'})
export class FileMetaDataService {
    private readonly fileMetaDataUrl: string;

    constructor(private http: HttpClient, private logger: NGXLogger) {
        this.fileMetaDataUrl = 'http://localhost:8081/api/files';
    }

    public getAllFileMetaData(): Observable<FileMetaData[]> {
        this.logger.debug(`Trying to get all file meta data from endpoint ${this.fileMetaDataUrl}`);
        return this.http.get<FileMetaData[]>(this.fileMetaDataUrl);
    }
}