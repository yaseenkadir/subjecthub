import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UniversitySearchService} from '../services/university-search.service';
import { University} from '../models/university';

@Component({
    selector: 'app-universities',
    templateUrl: './universities.component.html',
    styleUrls: ['./universities.component.css'],
    providers: [UniversitySearchService]
})

export class UniversitiesComponent implements OnInit {

    universities: University[];
    fatalMessage: string = null;
    sysErrorMessageTemplate: string = "Universities system has encountered an error, please refresh.";
    message: string = null;

    constructor(private router: Router, private universitySearchService: UniversitySearchService) {

    }

    ngOnInit() {
        this.fetch();
    }

    cleanMessages(){
        this.fatalMessage = null;
        this.message = null;
    }

    goToUniversitySearchpage(university) {
        console.log(university);
        this.router.navigate([`/university/${university.id}/search`]);
    }


    fetch(){
        this.cleanMessages();
        this.universitySearchService.getUniversities()
            .then(result => {
                if(result.isSuccessful()){
                    this.universities = result.response;
                } else {
                    this.fatalMessage = result.response[result.message];
                }
            })
            .catch(e => {
                this.fatalMessage = this.sysErrorMessageTemplate;
                console.log(e)
            });
    }
}
