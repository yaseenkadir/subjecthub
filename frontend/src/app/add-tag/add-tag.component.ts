import { Component, OnInit } from '@angular/core';

import {AddTagService} from '../services/add-tag.service';
import {Tag} from "../models/Tag";

@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.css'],
    providers:[AddTagService]
})
export class AddTagComponent implements OnInit {

  constructor(private tagService: AddTagService) { }

  ngOnInit() {
      this.tagService.createTag(1, 1, new Tag("Test"))
          .then(t => console.log(t))
          .catch(err => console.log(err));
  }

}
