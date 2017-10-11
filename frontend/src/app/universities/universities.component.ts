import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UniversitySearchService} from '../services/university-search.service';
import { University} from '../models/university';
import { Utils } from '../utils/utils';

@Component({
    selector: 'app-universities',
    templateUrl: './universities.component.html',
    styleUrls: ['./universities.component.css'],
    providers: [UniversitySearchService]
})

export class UniversitiesComponent implements OnInit {

    universities: University[];
    errorMessage: string = null;

    constructor(private router: Router, private universitySearchService: UniversitySearchService) {

    }

    ngOnInit() {
        this.fetch();
    }

    cleanMessages(){
        this.errorMessage = null;
    }

    goToUniversitySearchpage(university) {
        console.log(university);
        this.router.navigate([`/university/${university.id}/search`]);
    }


    fetch(){
        this.cleanMessages();
        this.universitySearchService.getUniversities()
            .then(universities => {
                this.universities = universities;
            })
            .catch(e => {
                this.errorMessage = Utils.getApiErrorMessage(e);
            });
    }
}
