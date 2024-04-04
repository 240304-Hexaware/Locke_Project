import { Component, OnChanges, signal, SimpleChanges } from '@angular/core';
import { FilesService } from '../files.service';
import { NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-filters',
  standalone: true,
  imports: [NgFor, FormsModule],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.css'
})
export class FiltersComponent {
  filesService: FilesService

  specId = ""

  constructor(filesService: FilesService) {
    this.filesService = filesService
  }

  specChange(id: any) {
    if(id != "") {
      this.filesService.filterCriteria = this.filesService.recordFilters.filter(filter => filter.name != "id")
      this.filesService.recordFilters.push({"name": "id", 
        values: this.filesService.specs.find(x => x.id == id)?.recordIds!
      })

      this.filesService.filteredRecords = this.filesService.applyFilters()
    } else {
      this.filesService.filterCriteria = this.filesService.recordFilters.filter(filter => filter.name != "id")
      this.filesService.filteredRecords = this.filesService.applyFilters()
    }
  }

  resetFilters() {
    this.filesService.filterCriteria = []
    this.filesService.filteredRecords = this.filesService.applyFilters()
  }
}
