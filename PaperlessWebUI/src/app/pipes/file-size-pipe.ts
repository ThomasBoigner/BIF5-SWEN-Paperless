import {Pipe, PipeTransform} from "@angular/core";

@Pipe({name: 'fileSize'})
export class FileSizePipe implements PipeTransform {
    transform(size: number): string {
        if (size < 1000) {
            return `${size.toFixed(0)} B`;
        }

        const units = ['B', 'kB', 'MB', 'GB'];
        let unitIndex = 0;
        let formattedSize = size;

        while (formattedSize >= 1000 && unitIndex < units.length - 1) {
            formattedSize /= 1000;
            unitIndex++;
        }

        let displayValue = formattedSize.toFixed(1);
        if (displayValue.endsWith('.0')) {
            displayValue = displayValue.slice(0, -2);
        }

        return `${displayValue} ${units[unitIndex]}`;
    }
}