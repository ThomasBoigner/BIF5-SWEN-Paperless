import { SummarySlicePipe } from './summary-slice-pipe';

describe('Summary Slice Pipe', () => {
    const summarySlicePipe = new SummarySlicePipe();

    it('should return summary under 5 words', () => {
        // Given
        const summary = 'this is a summary';

        // When
        const result = summarySlicePipe.transform(summary);

        expect(result).toEqual(summary);
    });

    it('should shorten summary over 10 words', () => {
        // Given
        const summary = 'this is a very long summary that has over 10 words';

        // When
        const result = summarySlicePipe.transform(summary);

        expect(result).toEqual('this is a very long summary that has over 10 ...');
    });
});
