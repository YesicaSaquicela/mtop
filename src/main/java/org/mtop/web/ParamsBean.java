/**
* This file is part of Glue: Adhesive BRMS
*
* Copyright (c)2012 José Luis Granda <jlgranda@eqaula.org> (Eqaula Tecnologías Cia Ltda)
* Copyright (c)2012 Eqaula Tecnologías Cia Ltda (http://eqaula.org)
*
* If you are developing and distributing open source applications under
* the GNU General Public License (GPL), then you are free to re-distribute Glue
* under the terms of the GPL, as follows:
*
* GLue is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Glue is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Glue. If not, see <http://www.gnu.org/licenses/>.
*
* For individuals or entities who wish to use Glue privately, or
* internally, the following terms do not apply:
*
* For OEMs, ISVs, and VARs who wish to distribute Glue with their
* products, or host their product online, Eqaula provides flexible
* OEM commercial licenses.
*
* Optionally, Customers may choose a Commercial License. For additional
* details, contact an Eqaula representative (sales@eqaula.org)
*/
package org.mtop.web;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @adapter <a href="mailto:jlgranda81@gmail.com">José Luis Granda</a> 
 */
@Named("params")
@RequestScoped
public class ParamsBean
{
   private int featureNumber;
   private int iterationNumber;
   private String projectSlug;
   private String profileUsername;
   private int storyNumber;

   public String getProjectSlug()
   {
      return projectSlug;
   }

   public void setProjectSlug(final String projectSlug)
   {
      this.projectSlug = projectSlug;
   }

   public String getProfileUsername()
   {
      return profileUsername;
   }

   public void setProfileUsername(final String profileUsername)
   {
      this.profileUsername = profileUsername;
   }

   public int getStoryNumber()
   {
      return storyNumber;
   }

   public void setStoryNumber(final int storyNumber)
   {
      this.storyNumber = storyNumber;
   }

   public int getIterationNumber()
   {
      return iterationNumber;
   }

   public void setIterationNumber(final int iterationNumber)
   {
      this.iterationNumber = iterationNumber;
   }

   public int getFeatureNumber()
   {
      return featureNumber;
   }

   public void setFeatureNumber(final int featureNumber)
   {
      this.featureNumber = featureNumber;
   }
}
