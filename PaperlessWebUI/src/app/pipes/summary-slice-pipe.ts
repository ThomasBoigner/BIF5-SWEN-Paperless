import {Pipe, PipeTransform} from "@angular/core";

@Pipe({name: 'summarySlice'})
export class SummarySlicePipe implements PipeTransform {
    transform(summary: string | undefined): string {
        if (!summary) {
          return '';
        }

        const words = summary.split(' ')

        if (words.length <= 5) {
          return summary;
        }

        return `${words.slice(0, 10).join(' ')} ...`;
    }
}