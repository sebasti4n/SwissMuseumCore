/*
 * Copyright 2012-2013 Sebastien Zurfluh
 * 
 * This file is part of "Parcours".
 * 
 * "Parcours" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * "Parcours" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with "Parcours".  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.sebastienzurfluh.swissmuseum.core.client.view.pagewidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.media.client.Video;

import ch.sebastienzurfluh.swissmuseum.core.client.control.eventbus.EventBus;
import ch.sebastienzurfluh.swissmuseum.core.client.control.eventbus.events.PageModifiedEvent;
import ch.sebastienzurfluh.swissmuseum.core.client.control.eventbus.events.ResourceRequest;
import ch.sebastienzurfluh.swissmuseum.core.client.model.Model;
import ch.sebastienzurfluh.swissmuseum.core.client.model.structure.DataReference;
import ch.sebastienzurfluh.swissmuseum.core.client.model.structure.ResourceData;
import ch.sebastienzurfluh.swissmuseum.core.client.patterns.Observable;
import ch.sebastienzurfluh.swissmuseum.core.client.patterns.Observer;

/**
 * The ResourceWidget can be added to any widget and will automatically requests it's data
 * and wait for it to be available before rendering.
 *
 *
 * @author Sebastien Zurfluh
 *
 */
public class ResourceWidget extends SimplePanel implements Observer {
	private Model model;
	private DataReference reference;
	private EventBus eventBus;
	
	private SimplePanel resourceContainer = new SimplePanel(new Image("resources/images/icons/loading.gif"));
	private Label title = new Label();
	private Label details = new Label();
	
	
	// Styles
	private String primaryStyle = "pageWidget-resource";
	private String containerExtension = "-container";
	private String titleExtension = "-title";
	private String detailsExtension = "-details";
	private String imageExtension = "-image";
	private String videoExtension = "-video";
	
	public ResourceWidget(DataReference reference, EventBus eventBus, Model model) {
		System.out.println("ResourceWidget: new resource widget " + reference);
		this.model = model;
		this.reference = reference;
		this.eventBus = eventBus;
		
		setStyleName(primaryStyle+containerExtension);
		title.setStyleName(primaryStyle+titleExtension);
		details.setStyleName(primaryStyle+detailsExtension);
		
		FlowPanel innerLayout = new FlowPanel();
		
		innerLayout.add(resourceContainer);
		innerLayout.add(title);
		innerLayout.add(details);
		
		add(innerLayout);
		
		model.allNeededResourcesObservable.subscribeObserver(this);
		
		// request data AFTER WE START LISTENING
		eventBus.fireEvent(new ResourceRequest(reference));
	}

	@Override
	public void notifyObserver(Observable source) {
		for (ResourceData resource : model.getAllNeededResources()) {
			// The following check is sufficient to determine unicity as there cannot be
			// two different resources with the same id, be it an IMAGE and a VIDEO.
			// This is explicitly reflected in the CakeConnector/MySQL by the use of a single
			// table Resources with an unique key column 'id'.
			if (resource.getReference().equals(this.getReference())) {
				switch(resource.getResourceType()) {
				case IMAGE:
					Image image = new Image(resource.getURL());
					image.setStyleName(primaryStyle+imageExtension);
					image.setAltText(resource.getTitle());
					
					final ResourceGallery imageGallery =
							new ResourceGallery(
									image.getUrl(),
									resource.getTitle(),
									resource.getDetails());
					image.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							imageGallery.center();
						}
					});
					resourceContainer.setWidget(image);
					break;
				case VIDEO:
					int id = Random.nextInt();
					HTML video = new HTML(
							"<a href='" + resource.getURL() + "' target='_blank'>" +
							"<video id='" + id + "' autobuffer class='" +
							primaryStyle+videoExtension + "'>" +
							"<source src='" + resource.getURL() + "' " +
							"type='video/ogg; codecs=\"theora, vorbis\"'/>" +
							"</video></a>");
					resourceContainer.setWidget(video);
					
//					Video video = Video.createIfSupported();
//					if (video == null) {
//						resourceContainer.setWidget(new Label("Your browser is not able to play"
//								+ " this video"));
//					} else {
//						video.setSrc(resource.getURL());
//						video.setStyleName(primaryStyle+videoExtension);
//						resourceContainer.setWidget(video);
//					}
					break;
				default:
					// Destroy the object. It is never referenced outside of this object, but is
					// attached to it's parent.
					this.removeFromParent();
				}
				
				
				title.setText(resource.getTitle());
				details.setText(resource.getDetails());
				
				eventBus.fireEvent(new PageModifiedEvent());
				
				model.allNeededResourcesObservable.unsubscribeObserver(this);
				return;
			}
		}
	}

	private DataReference getReference() {
		return reference;
	}
	
}
