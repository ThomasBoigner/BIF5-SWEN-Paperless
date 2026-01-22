export interface FileMetaData {
    fileToken: string;
    creationDate: Date;
    fileName: string;
    fileSize: number;
    description?: string;
    numberOfAccesses: number;
    fullText?: string;
    summary?: string;
}
