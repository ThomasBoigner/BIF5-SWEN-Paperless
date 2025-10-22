export interface FileMetaData {
    fileToken: string;
    creationDate: Date;
    fileName: string;
    fileSize: number;
    description?: string;
    fullText?: string;
    summary?: string;
}
