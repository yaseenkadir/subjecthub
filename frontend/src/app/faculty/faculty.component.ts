import { Component, OnInit } from '@angular/core';
import {FacultySearchService} from "../services/faculty-search.service";
import {Faculty} from "../models/faculty";

@Component({
    selector: 'app-faculty',
    templateUrl: './faculty.component.html',
    styleUrls: ['./faculty.component.css'],
    providers: [FacultySearchService]
})
export class FacultyComponent implements OnInit {

    faculties: Faculty[];
    fatalMessage: string = null;
    sysErrorMessageTemplate: string = "Faculties system has encountered an error, please refresh";
    message: string = null;
    universityId = 1;


    constructor(private facultySearchService: FacultySearchService) { }

    ngOnInit() {
        // this.fetch();
        this.facultySearchService.fetchFaculties(this.universityId).then(faculties =>
            this.faculties = faculties
        )
    }

    // cleanMessages(){
    //     this.fatalMessage = null;
    //     this.message = null;
    // }
    //
    // fetch(){
    //     this.cleanMessages();
    //     this.facultySearchService.fetch()
    //         .then(result => {
    //             if(result.isSuccessful()){
    //                 this.faculties = result.response;
    //             } else {
    //                 this.fatalMessage = result.response['message'];
    //             }
    //         })
    //         .catch(e => {
    //             this.fatalMessage = this.sysErrorMessageTemplate;
    //             console.log(e)
    //         });
    // }

}
