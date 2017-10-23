import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UniversityService} from '../services/university.service';
import { University} from '../models/university';
import { Utils } from '../utils/utils';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-universities',
    templateUrl: './universities.component.html',
    styleUrls: ['./universities.component.css'],
    providers: [UniversityService, AuthService],
})

export class UniversitiesComponent implements OnInit {

    universities: University[];
    errorMessage: string = null;
    isAdmin: boolean;
    isLoading: boolean;

    constructor(protected router: Router, protected universityService: UniversityService,
                protected authService: AuthService, protected toastr: ToastrService) {
    }

    ngOnInit() {
        this.universities = [];
        this.fetch();
        this.isAdmin = this.authService.isAdmin();
    }

    cleanMessages(){
        this.errorMessage = null;
    }

    goToUniversitySearchpage(university) {
        console.log(university);
        this.router.navigate([`/university/${university.id}/search`]);
    }


    fetch(){
        this.isLoading = true;
        this.cleanMessages();
        this.universityService.getUniversities()
            .then(universities => {
              this.isLoading = false;
              this.universities = universities;
            })
            .catch(e => {
                this.isLoading = true;
                this.errorMessage = Utils.getApiErrorMessage(e);
            });
    }
}
