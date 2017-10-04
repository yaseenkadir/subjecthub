import { Component, OnInit } from '@angular/core';
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

    constructor(private universitySearchService: UniversitySearchService) {

    }

    ngOnInit() {
        this.fetch();
    }

    cleanMessages(){
        this.fatalMessage = null;
        this.message = null;
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
