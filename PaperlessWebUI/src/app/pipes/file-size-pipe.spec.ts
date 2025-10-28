import { FileSizePipe } from './file-size-pipe';

describe('File Size Pipe', () => {
    const fileSizePipe = new FileSizePipe();

    it('FileSizePipe should transform 100 to 100 B', () => {
        // Given
        const fileSize = 100;

        // When
        const result = fileSizePipe.transform(fileSize);

        // Then
        expect(result).toEqual('100 B');
    });

    it('FileSizePipe should transform 1000 to 1 kB', () => {
        // Given
        const fileSize = 1001;

        // When
        const result = fileSizePipe.transform(fileSize);

        // Then
        expect(result).toEqual('1 kB');
    });

    it('FileSizePipe should transform 1234 to 1.2 kB', () => {
        // Given
        const fileSize = 1234;

        // When
        const result = fileSizePipe.transform(fileSize);

        // Then
        expect(result).toEqual('1.2 kB');
    });

    it('FileSizePipe should transform 1000000 to 1 MB', () => {
        // Given
        const fileSize = 1000001;

        // When
        const result = fileSizePipe.transform(fileSize);

        // Then
        expect(result).toEqual('1 MB');
    });

    it('FileSizePipe should transform 1234000 to 1.2 MB', () => {
        // Given
        const fileSize = 1234001;

        // When
        const result = fileSizePipe.transform(fileSize);

        // Then
        expect(result).toEqual('1.2 MB');
    });

    it('FileSizePipe should transform 1000000000 to 1 GB', () => {
        // Given
        const fileSize = 1000000001;

        // When
        const result = fileSizePipe.transform(fileSize);

        // Then
        expect(result).toEqual('1 GB');
    });

    it('FileSizePipe should transform 1234000000 to 1.2 GB', () => {
        // Given
        const fileSize = 1234000001;

        // When
        const result = fileSizePipe.transform(fileSize);

        // Then
        expect(result).toEqual('1.2 GB');
    });
});
