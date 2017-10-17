import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import {AddTagService} from '../services/add-tag.service';
import {Tag} from "../models/Tag";
import { Utils } from '../utils/utils';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.css'],
    providers:[AddTagService]
})
export class AddTagComponent {
  @Input()
  universityId: number;

  @Input()
  subjectId: number;

  error: string;
  success: string;
  tag: Tag;
  isLoading: boolean;

  isLoggedIn: boolean;

  constructor(private tagService: AddTagService, private router: Router, private authService: AuthService) {
    this.tag = new Tag('');
    this.isLoading = false;
    this.isLoggedIn = this.authService.isLoggedIn();
  }
  clearMessages() {

      this.error = '';
      this.success = '';
  }

  clearField() {
      this.isLoading = false;
      this.tag.name = '';
  }

  submit() {
      this.clearMessages();
      if (!this.tag.name) this.error = "A Tag Name must be supplied";
      else {
          this.isLoading = true;

            this.tagService.createTag(this.universityId, this.subjectId, this.tag)
              .then(subject => {
                  this.success = "Tag added successfully";
                  this.clearField();
                  location.reload();
              })
              .catch(err => {
                  this.error = Utils.getApiErrorMessage(err);
                  this.clearField();
              })


      }
  }


}
